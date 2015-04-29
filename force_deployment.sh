#!/bin/bash

date > force_deploy.lst
git reset
git add -f force_deploy.lst
git commit -nm 'forcing deployment'
echo "promote to deploy now!"
