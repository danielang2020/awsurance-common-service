aws dynamodb create-table --table-name RightTimeInfo \
                          --key-schema AttributeName=pk,KeyType=HASH AttributeName=sk,KeyType=RANGE \
                          --attribute-definitions AttributeName=pk,AttributeType=S AttributeName=sk,AttributeType=S \
                          --endpoint-url http://localhost:4566 \
                          --billing-mode PAY_PER_REQUEST \
                          --region us-west-2