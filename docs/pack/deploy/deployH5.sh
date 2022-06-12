#!/bin/bash

cd /data/project/h5

git pull

npm run build

cp -rf /data/project/h5/dist /data/project/h5
