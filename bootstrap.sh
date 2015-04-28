#!/bin/bash
echo 'select 1' | mysql -uroot
if [ $? -eq 0 ] ; then
	echo 'drop database if exists `pricing_kaizen`' | mysql -uroot
	echo 'create database `pricing_kaizen`;' | mysql -uroot
fi
