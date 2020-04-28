package com.crypteron.cipherdb.sample;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleApp {
  private static final Logger LOG = LoggerFactory.getLogger(ProcessCustomer.class);
  private final InputStream   in;
  private final PrintStream   out;

  public SampleApp(final InputStream in, final PrintStream out) {
    super();
    this.in = in;
    this.out = out;
  }

  private void printMenu() {
    this.out.println("====================================================");
    this.out.println("R : Read all customer orders in table");
    this.out.println("+ : Auto-create a new customer order");
    this.out.println("F : Auto-create 10 customer orders");
    this.out.println("? : Search encrypted customer names");
    this.out.println("! : Wipe all customer orders via direct SQL");
    this.out.println("N : No CipherDB => data is secure/undecipherable");
    this.out.println("S : Seal/Unseal API");
    this.out.println("Q : Quit");

    this.out.print("\nEnter your selection: ");
  }

  public void run() {
    final ProcessCustomer cust = new ProcessCustomer(this.in, this.out);
    final BufferedReader reader = new BufferedReader(new InputStreamReader(this.in));
    boolean ongoing = true;
    while (ongoing) {
      printMenu();
      try {
        final Optional<String> userInput = Optional.of(reader.readLine());
        this.out.println();
        final char option = userInput.map(String::toLowerCase).filter(s -> !s.isEmpty()).map(string -> string.charAt(0))
            .orElse(Character.MIN_VALUE);
        switch (option) {
          case '+':
            cust.createAuto(1);
            break;
          case 'f':
            cust.createAuto(10);
            break;
          case 'r':
            cust.readAll();
            break;
          case '?':
            cust.search();
            break;
          case 'n':
            cust.readAllInsecure();
            break;
          case '!':
            cust.deleteAll();
            break;
          case 's':
            cust.secureObject();
            break;
          case 'q':
            ongoing = false;
            break;
          default:
            this.out.println("Unknown option: " + option);
            break;
        }
      } catch (final Exception e) {
        LOG.error("uh oh", e);
        ongoing = false;
      }
      this.out.println();
    }
    cust.close();
  }

}
