pipeline {
    agent any

    tools {
        maven 'Maven' // Đặt tên như bạn cài trong Jenkins
        jdk 'JDK17'   // Đặt theo tên JDK bạn khai báo
    }

    environment {
        SONARQUBE = 'SonarQube' // Tên SonarQube server trong Jenkins
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
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar -Dsonar.projectKey=library-ci-cd -Dsonar.host.url=http://localhost:9000 -Dsonar.login=your_token'
                }
            }
        }

        stage('Docker Build & Push') {
            steps {
                sh 'docker build -t hoangmai29/library-ci-cd:latest .'
                sh 'docker push hoangmai29/library-ci-cd:latest'
            }
        }

        stage('Deploy') {
            steps {
                sh 'ssh user@your-server "docker pull hoangmai29/library-ci-cd:latest && docker-compose up -d"'
            }
        }
    }
}
