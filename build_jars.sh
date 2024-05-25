# Define the destination directory (use forward slashes for Git Bash compatibility)
DESTINATION_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"/compiled_jars
SUFFIX_PATTERN="-[0-9]*.[0-9]*.[0-9]*-SNAPSHOT"

# Ensure the destination directory exists
mkdir -p "$DESTINATION_DIR"

# Traverse each subdirectory and run mvn package
for dir in */ ; do
    if [ "$dir" != "compiled_jars/" ] && [ -d "$dir" ]; then
        echo "Entering directory: $dir"
        cd "$dir" || exit
        
        # Run mvn package
        mvn package
        
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
