pipeline {
    agent {
        docker {
            image 'maven:3.8.5-openjdk-17'
            args '-v /var/jenkins_home/workspace/src:/app'
        }
    }

    environment {
        SONAR_SCANNER_HOME = tool 'sonar-scanner'
        SONAR_HOST_URL = 'http://sonarqube:9000'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Abdelouafi-oubenali/jenkins-test',
                    credentialsId: 'github-credentials'
            }
        }

        stage('Build & Compile') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    jacoco(
                        execPattern: 'target/jacoco.exec',
                        classPattern: 'target/classes',
                        sourcePattern: 'src/main/java'
                    )
                }
            }
        }

        stage('Code Analysis - SonarQube') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh '''
                    mvn sonar:sonar \
                      -Dsonar.projectKey=java-demo-app \
                      -Dsonar.projectName="Java Demo Application" \
                      -Dsonar.host.url=http://sonarqube:9000 \
                      -Dsonar.login=admin \
                      -Dsonar.password=admin
                    '''
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 1, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Packaging') {
            steps {
                sh 'mvn package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    post {
        always {
            emailext (
                subject: "Build ${currentBuild.result}: Job ${env.JOB_NAME}",
                body: """
                Build: ${env.JOB_NAME} - ${env.BUILD_NUMBER}
                Result: ${currentBuild.result}
                URL: ${env.BUILD_URL}
                """,
                to: "dev-team@company.com"
            )
        }
        success {
            echo 'Pipeline exécuté avec succès!'
        }
        failure {
            echo 'Pipeline a échoué!'
        }
    }
}