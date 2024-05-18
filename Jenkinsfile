pipeline {
  agent any
  environment {

    }

  stages {
    stage('Git Checkout') {
              steps {
                  checkout scm
                  echo 'Git Checkout Success!'
              }
    }

    stage('Test') {
        steps {
            sh './gradlew test'
            echo 'test success'
        }
    }

    stage('Build') {
      steps {
           sh './gradlew clean build --exclude-task test --exclude-task asciidoctor'
           echo 'build success'
      }
    }
  }
}