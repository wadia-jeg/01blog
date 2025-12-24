# Go to https://maven.apache.org/download.cgi and choose the version you want to install 
wget https://dlcdn.apache.org/maven/maven-3/3.9.11/binaries/apache-maven-3.9.11-bin.tar.gz

# Create ~/maven folder if does not exist
mkdir ~/maven

# Extarct the tar 
tar -xzvf apache-maven-3.9.11-bin.tar.gz -C ~/maven

# Add Maven's bin directory to your PATH environment variable so that you can run Maven from the command line.
# nano ~/.bashrc
# for zsh
# nano ~/.zshrc 

# Add the following line 
# export M2_HOME=~/maven/apache-maven-3.9.11
# export PATH=$M2_HOME/bin:$PATH

# Or write directly 
echo "export M2_HOME=~/maven/apache-maven-3.9.11\nexport PATH=$M2_HOME/bin:$PATH" >> ~/.zshrc

# Apply changes
source ~/.bashrc

# Test 
mvn -v


