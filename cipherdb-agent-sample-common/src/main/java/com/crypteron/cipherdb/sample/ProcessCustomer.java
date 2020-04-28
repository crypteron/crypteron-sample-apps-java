package com.crypteron.cipherdb.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.xml.bind.DatatypeConverter;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.crypteron.CipherObject;
import com.crypteron.SecureSearch;
import com.crypteron.ciphercore.config.ConfigurationParameter;
import com.crypteron.ciphercore.config.CrypteronConfiguration;
import com.crypteron.cipherdb.entity.Customer;
import com.crypteron.cipherdb.entity.CustomerHelper;

public class ProcessCustomer {
  private static final String        PERSISTENCE_UNIT_NAME = "com.crypteron.cipherdb.sample.testdb";
  private final InputStream          in;
  private final PrintStream          out;
  private final ObjectMapper         mapper;
  private final EntityManagerFactory secureEMFactory;
  private final EntityManagerFactory plainEMFactory;

  public ProcessCustomer(final InputStream in, final PrintStream out) {
    this.in = in;
    this.out = out;
    this.mapper = new ObjectMapper();
    this.out.println("Creating secure database session factory ...");
    this.secureEMFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    // Set the App Secret aside for a moment so we can create an insecure EntityManagerFactory that doesn't use
    // Crypteron
    final String savedAppSecret = CrypteronConfiguration.getAppSecret().get();
    // Remove App Secret from configuration. Only for demo purposes. Do not use in production.
    CrypteronConfiguration.getInstance().remove(ConfigurationParameter.APP_SECRET.getConfigurationKey());
    this.out.println("Creating regular database session factory ...");
    this.plainEMFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    // Restoring App Secret so other Crypteron features (like CipherObject) are still enabled.
    CrypteronConfiguration.setAppSecret(savedAppSecret);
  }

  public void close() {
    this.secureEMFactory.close();
    this.plainEMFactory.close();
  }

  private void createAndRunWithEntityManager(final EntityManagerFactory entityManagerFactory,
      final Consumer<EntityManager> run) {
    final EntityManager entityManager = entityManagerFactory.createEntityManager();
    run.accept(entityManager);
    entityManager.close();
  }

  private void withPlainEntityManager(final Consumer<EntityManager> run) {
    createAndRunWithEntityManager(this.plainEMFactory, run);
  }

  private void withSecureEntityManager(final Consumer<EntityManager> run) {
    createAndRunWithEntityManager(this.secureEMFactory, run);
  }

  public void createAuto(final int numToAdd) {
    this.out.println("[[CipherDB] Adding customer records]");
    this.out.print('[');

    withSecureEntityManager(entityManager -> {
      entityManager.getTransaction().begin();
      for (int i = 0; i < numToAdd; i++) {
        entityManager.persist(CustomerHelper.randomCustomer());
        this.out.print('.');
      }
      entityManager.getTransaction().commit();
    });

    this.out.println(']');
  }

  private void readAllFromEntityManager(final EntityManager entityManager, final boolean rawBits) {
    entityManager.getTransaction().begin();
    final CriteriaQuery<Customer> queryCriteria = entityManager.getCriteriaBuilder().createQuery(Customer.class);
    queryCriteria.from(Customer.class);
    final TypedQuery<Customer> selectQuery = entityManager.createQuery(queryCriteria);
    final List<Customer> customers = selectQuery.getResultList();
    final String allCustomersString = customersDisplayString(customers, rawBits);
    this.out.println(allCustomersString);
    entityManager.getTransaction().commit();
  }

  public void readAll() {
    withSecureEntityManager(entityManager -> readAllFromEntityManager(entityManager, true));
  }

  public void readAllInsecure() {
    this.out.println(
        "Without CipherDB, data is secured. Even the database administrator cannot see the data.");
    withPlainEntityManager(entityManager -> readAllFromEntityManager(entityManager, false));
  }

  private String customersDisplayString(final Collection<Customer> customers, final boolean rawBits) {
    return customers.stream().map(customer -> customerDisplayString(customer, rawBits))
        .collect(Collectors.joining(System.lineSeparator().concat(System.lineSeparator())));
  }

  private String customerDisplayString(final Customer customer, final boolean rawBits) {
    final StringBuilder displayString = new StringBuilder();
    String ssnString;

    if (customer.getSecureSocialSecurityNumber() == null) {
      ssnString = "null";
    } else if (rawBits) {
      ssnString = new String(customer.getSecureSocialSecurityNumber(), StandardCharsets.UTF_8);
    } else {
      ssnString = DatatypeConverter.printBase64Binary(customer.getSecureSocialSecurityNumber());
    }
    final String description = String.format("OrderId:%s, %s got %s at %s using CC %s, SSN=%s, PIN=%s",
        customer.getOrderId(), customer.getCustomerName(), customer.getOrderItem(), customer.getTimestamp(),
        customer.getSecureCreditCardNumber(), ssnString, customer.getSecureLegacyPin());
    displayString.append(description);
    displayString.append(System.lineSeparator());
    displayString.append("---------------------------------------------------------------");
    return displayString.toString();
  }

  public void deleteAll() {
    withSecureEntityManager(entityManager -> {
      entityManager.getTransaction().begin();
      final CriteriaDelete<Customer> deleteCriteria = entityManager.getCriteriaBuilder()
          .createCriteriaDelete(Customer.class);
      deleteCriteria.from(Customer.class);
      final Query deleteQuery = entityManager.createQuery(deleteCriteria);
      final int deleted = deleteQuery.executeUpdate();
      entityManager.getTransaction().commit();
      this.out.println("deleted " + deleted + " customer orders");
    });
  }

  public void secureObject() throws JsonGenerationException, JsonMappingException, IOException {
    final Customer customer = CustomerHelper.randomCustomer();
    this.out.println("serialized object before seal():");
    this.out.println(this.mapper.writeValueAsString(customer));
    this.out.println();

    CipherObject.seal(customer);
    this.out.println("serialized object after seal():");
    this.out.println(this.mapper.writeValueAsString(customer));
    this.out.println();

    CipherObject.unseal(customer);
    this.out.println("serialized object after unseal():");
    this.out.println(this.mapper.writeValueAsString(customer));
  }

  public void search() throws IOException {
    this.out.print("Enter a name to search for: ");
    final BufferedReader reader = new BufferedReader(new InputStreamReader(this.in));
    final String searchName = reader.readLine();
    final String searchPrefix = SecureSearch.getPrefix(searchName);
    withSecureEntityManager(entityManager -> {
      final TypedQuery<Customer> query = entityManager
          .createQuery("SELECT c FROM Customer c where c.customerName LIKE :searchPrefix", Customer.class);
      query.setParameter("searchPrefix", searchPrefix + "%");
      final String foundCustomersString = customersDisplayString(query.getResultList(), true);
      this.out.println("Found these customers: ");
      this.out.println(foundCustomersString);
    });

  }
}
