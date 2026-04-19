pipeline {
    agent any

    stages {
        stage('Build and Test') {
            steps {
                dir('EcommerceAutomation4') {
                    bat 'mvn clean test'
                }
            }
        }
    }
}