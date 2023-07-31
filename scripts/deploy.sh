version: 0.0
os: linux

files:
  - source: /
    destination: /home/ubuntu/finote/finote-backend
    overwrite: yes
permissions:
  - object: /home/ubuntu/finote/finote-backend/
    owner: ubuntu
    group: ubuntu
hooks:
  AfterInstall:
    - location: scripts/deploy.sh
      timeout: 60
      runas: ubuntu