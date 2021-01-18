export PG_DB=ose_db
export PG_HOST=192.168.1.95
export PG_PASSWORD=1234
export PG_PORT=5432
export PG_USER=postgres


export MONGO_DB=ose_db
export MONGO_HOST=192.168.1.95
export MONGO_PORT=27017

export MONGO_SEC_URI=192.168.1.95
export MONGO_SEC_DB=osedb

export BUCKET_NAME=demo-ose-nubefact-com
export AWS_S3USER=AKIAIOSFODNN7EXAMPLE
export AWS_S3PWD=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
export AWS_S3URL=https://s3.nubefact.com

mvn spring-boot:run
