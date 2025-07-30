pipeline {
    agent any

    tools {
        maven 'Maven 3.9.6'  // Đặt đúng tên Maven bạn đã khai báo trong Jenkins
        jdk 'jdk-17'         // Hoặc jdk-21 tùy bạn đã cài
    }

    environment {
        GIT_REPO = 'https://github.com/hoangmai29/library-ci-cd.git'
    }

    stages {
        stage('Clone') {
            steps {
                git "${env.GIT_REPO}"
            }
        }

        stage('Build & SonarQube') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    bat '''
                        mvn clean verify sonar:sonar ^
                        -Dsonar.projectKey=library-ci-cd ^
                        -Dsonar.host.url=%SONAR_HOST_URL% ^
                        -Dsonar.login=%SONAR_AUTH_TOKEN%
                    '''
                }
            }
        }

        stage('Package') {
            steps {
                bat 'mvn package'
            }
        }
    }
}
