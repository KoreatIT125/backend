pipeline {
    agent any
    
    tools {
        jdk 'JDK17'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                sh './gradlew clean build -x test'
            }
        }
        
        stage('Test') {
            steps {
                sh './gradlew test'
            }
            post {
                always {
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }
        
        stage('Docker Build') {
            steps {
                script {
                    docker.build("disaster-safety-backend:${env.BUILD_NUMBER}")
                }
            }
        }
        
        stage('Deploy') {
            when {
                branch 'main'
            }
            steps {
                echo 'Deploying to production...'
                // TODO: 배포 스크립트 추가
            }
        }
    }
    
    post {
        success {
            echo 'Backend CI/CD 성공!'
        }
        failure {
            echo 'Backend CI/CD 실패!'
        }
    }
}
