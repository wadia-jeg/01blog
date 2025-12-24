wget https://download.oracle.com/java/21/latest/jdk-21_linux-x64_bin.tar.gz

mkdir ~/java/

tar -xzvf jdk-21_linux-x64_bin.tar.gz -C ~/java/

echo "export JAVA_HOME=~/java/jdk-21\nexport PATH=$JAVA_HOME/bin:$PATH" >> ~/.zshrc

source ~/.zshrc


