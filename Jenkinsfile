pipeline {
    agent any

    tools {
        maven 'Maven 3.9.6'
        jdk 'JDK 17'
    }

    environment {
        SONARQUBE = 'SonarQube'
        SSH_CREDENTIALS_ID = 'ssh-key-library-ci-cd'
        REMOTE_HOST = 'your.server.ip'
        REMOTE_USER = 'youruser'
        REMOTE_PATH = '/home/youruser/library-ci-cd'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']],
                    userRemoteConfigs: [[url: 'https://github.com/hoangmai29/library-ci-cd.git']]
                ])
            }
        }

        stage('Build') {
            steps {
                bat 'echo JAVA_HOME=%JAVA_HOME%'
                bat 'java -version'
                bat 'mvn clean install'
            }
        }
stage('SonarQube Analysis') {
    steps {
        withSonarQubeEnv('SonarQube') {
            bat 'mvn clean verify sonar:sonar'
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

        stage('Docker Build') {
            steps {
                bat 'docker build -t library-ci-cd .'
            }
        }

        stage('Deploy Local with Docker Compose') {
            steps {
                bat 'docker-compose down || exit 0'
                bat 'docker-compose up -d --build'
            }
        }

        stage('Deploy to Remote Server via SSH') {
            steps {
                sshagent (credentials: ["${SSH_CREDENTIALS_ID}"]) {
                    bat """
                    ssh -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} ^ 
                        "cd ${REMOTE_PATH} && ^ 
                         git pull origin master && ^ 
                         docker-compose down || true && ^ 
                         docker-compose up -d --build"
                    """
                }
            }
        }
    }
}
