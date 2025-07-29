pipeline {
    agent any

    tools {
        maven 'Maven 3.9.6' // Hoặc tên cấu hình Maven bạn đã khai báo trong Jenkins
        jdk 'JDK 17'         // Hoặc JDK bạn đã setup trong Jenkins
    }

    environment {
        SONARQUBE = 'SonarQube' // Tên cấu hình SonarQube bạn đã khai trong Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/hoangmai29/library-ci-cd.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv("${SONARQUBE}") {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }
}
