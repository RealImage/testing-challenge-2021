### __GET THE LIST OF FILES IN YOUR ACCOUNT__

- Get files with `authorized` `token` and without `getSharedFiles` query parameter, check for *200* status code and validate response schema
- Get files with `authorized` `token` and with `getSharedFiles` query parameter, check for *200* status code and validate response schema
- Get files with`un-authorized` `token`, check for *401* status code and validate error response schema
- Get files with forbidden `token`, check for *403* status code and validate error response schema


### __UPLOAD ANY FILE INTO YOUR ACCOUNT__

- Upload a file with `authorized` `token`, check for *200* status code, response header and validate upload response schema
- Upload a file with `authorized` `token` and incorrect payload, check for *400* status code, response header and validate error response schema
- Upload a file upload with `authorized` `token` and without the required payload attributes(name, size, has, file), check for *400* status code, response header and validate error response schema
- Upload a file with un-`authorized` `token`, check for *401* status code, response header and validate error response schema
- Upload a file with `forbidden` `token`, check for *403* status code, response header and validate error response schema

### __DELETE ANY FILE FROM YOUR ACCOUNT__

- Delete a file with `authorized` `token` and fileId, check for *200* status code, response header and validate delete response schema
- Delete a file with `un-authorized` `token` and fileId, check for *401* status code, response header and validate error response schema

### __CHECK FILE COUNT__
- Get files
    - get the number of files
- Upload a file
    - get files count after upload
- Delete the uploaded file
    - get files count after deletion 
    - assert files count *before* and *after* upload.