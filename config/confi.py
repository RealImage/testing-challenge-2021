BASE_URI= "https://ec2-15-206-185-35.ap-south-1.compute.amazonaws.com/sharebox/api"
endpoints={
  "upload":"/upload",
  "files":"/files"}

upload_schema={
    "type" : "object",
    "properties" : {
        "fileId" : {"type" : "string"},
  "accessKey": {"type" : "string"},
  "secretKey": {"type" : "string"},
  "sessionToken": {"type" : "string"}
    },
    "required": ["fileId","accessKey","secretKey","sessionToken"]}
get_schema={
  "type": "object",
  "properties": {
    "status": {
      "type": "string"
    },
    "name": {
      "type": "string"
    },
    "fileHash": {
      "type": "string"
    },
    "createdOn": {
      "type": "string"
    },
    "bytesCompleted": {
      "type": "string"
    },
    "size": {
      "type": "string"
    },
    "fileId": {
      "type": "string"
    }
  }
}

getall_schema={
  "allOf": [
    {
      "$ref": "#/definitions/GetFiles"
    }
  ],
  "definitions": {
    "GetFiles": {
      "type": "array",
      "items": {
        "$ref": "#/definitions/GetUploadDetails"
      }
    },
    "GetUploadDetails": {
      "type": "object",
      "properties": {
        "status": {
          "type": "string"
        },
        "name": {
          "type": "string"
        },
        "fileHash": {
          "type": "string"
        },
        "createdOn": {
          "type": "string"
        },
        "bytesCompleted": {
          "type": "string"
        },
        "size": {
          "type": "string"
        },
        "fileId": {
          "type": "string"
        }
      }
    }
  }
}

