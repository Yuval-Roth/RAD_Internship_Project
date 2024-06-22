#!/bin/bash

# Define the destination directory (use forward slashes for Git Bash compatibility)
DESTINATION_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"/compiled_jars
SUFFIX_PATTERN="-[0-9]*.[0-9]*.[0-9]*-SNAPSHOT"

# List of directories to enter
ENTER_DIRS=("ms_nba/" "ms_news/" "ms_population/" "ms_service_discovery/" "ms_api_gateway/" "client_vaadin/")

# Function to check if a directory is in the enter list
should_enter() {
    local dir=$1
    for enter in "${ENTER_DIRS[@]}"; do
        if [[ "$dir" == "$enter" ]]; then
            return 1  # True, should enter
        fi
    done
    return 0  # False, should not enter
}

# Ensure the destination directory exists
mkdir -p "$DESTINATION_DIR"

# Run mvn package
mvn clean package -DskipTests -Pproduction

# Traverse each subdirectory and copy jar
for dir in */ ; do
    if [ -d "$dir" ] && ! should_enter "$dir"; then
        echo "Entering directory: $dir"
        cd "$dir" || exit
        
        # Find the resulting JAR files and copy them with a new prefix to the destination directory
        find . -name "*.jar" | while read -r jar; do
            basejar=$(basename "$jar")
            # Remove the suffix pattern from the base name
            newname="${basejar%%$SUFFIX_PATTERN.jar}.jar"
            cp "$jar" "$DESTINATION_DIR/$newname"
        done

        # Return to the parent directory
        cd ..
    fi
done

echo "Packaging and copying complete!"
