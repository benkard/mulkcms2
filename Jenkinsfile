// -*- mode: groovy -*-
pipeline {

  agent {
    kubernetes {
      cloud 'kubernetes'
      label 'mulkcms2-v1'
      yaml """
        apiVersion: v1
        kind: Pod
        metadata: {}
        spec:
          containers:
            - name: main
              image: adoptopenjdk:14-hotspot-bionic
              tty: true
              command:
                - /bin/cat
      }
      """
    }
  }

  stages {

    stage('Build & Test') {
      steps {
        container('main') {
          cache(maxCacheSize: 1000, caches: [
              [$class: 'ArbitraryFileCache', excludes: '', includes: '**/*', path: "$HOME/.m2"],
              [$class: 'ArbitraryFileCache', excludes: '', includes: '**/*', path: "$HOME/.yarn-cache"],
          ]) {
            ansiColor('xterm') {
              sh """
                apt-get -y update
                apt-get -y install --no-install-recommends npm
                npm install -g yarn
                yarn config set cache-folder $HOME/.yarn-cache
                ./mvnw package -Dquarkus.container-image.build=false -Dquarkus.container-image.push=false
              """
            }
          }
        }
      }
    }

    stage('Archive artifacts') {
      steps {
        container('main') {
          recordIssues tools: [
              mavenConsole(),
              java()
          ]
        }
      }
    }

  }
}
