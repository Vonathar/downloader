#!/bin/bash

project_dir="$HOME/Projects/Downloader"

cd "$project_dir" || exit

current_full_version=$(cat pom.xml | grep -ozP '(?<=<version>).*?(?=</version>\n</project>)')
current_minor_version=$(echo $current_full_version | grep -ozP '(?<=\.)\d{2}')

new_minor_version=$((current_minor_version + 1))
new_full_version="1.$new_minor_version"

search="    <version>$current_full_version</version>"
replace="    <version>$new_full_version</version>"

sed -i "s@$search@$replace@" pom.xml

echo "Current version: $current_full_version"
echo "New version: $new_full_version"

echo 'New line:' cat pom.xml | grep -ozP "<version>$new_full_version</version>"

mvn clean deploy -Dmaven.test.skip=true
