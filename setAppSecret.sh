#!/bin/sh

echo
echo "This is helper script replaces placeholders with your actual AppSecret "
echo "in the following files: "
echo
find . -iwholename "*src/main/resources/crypteron.properties"
echo
echo "You can directly edit those files too, see the readme.md for details."
echo
echo "What's your appSecret from the Crypteron Dashboard? "
read -p "AppSecret:" appSecret
echo
find . -iwholename "*src/main/resources/crypteron.properties" -exec echo "Patching" "{}" \; -exec sed -i -E "s/appSecret=(.*)/appSecret=${appSecret}/" "{}" \;
echo
echo "Done!"
echo 
