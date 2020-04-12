import binascii
import os

# token variables
# update the tokens before executing the script
token = "6676c1a1-2f97-4d71-a749-438fa993a6a9" # token ID for User 1
second_user_token = "06b52f43-4b15-47b5-b9cc-95867adc39ae"  # token ID for User 2
invalidToken = "362b0f98-58ea-4d82-89dc-a16cf55eb3f8"   # invalid token to validate 401 scenarios

# calculate a new random value
boundary = binascii.hexlify(os.urandom(16)).decode('ascii')


# function to prep the payload for multi-part form data
def encode_multipart_formdata(fields):
    body = (
            "".join('--%s\r\n'
                    'Content-Disposition: form-data; name="%s"\r\n'
                    '\r\n'
                    '%s\r\n' % (boundary, field, value)
                    for field, value in fields.items()) +
            "--%s--\r\n" % boundary
    )
    return body


# function to prep the content-type for post/put/delete requests
def encode_multipart_headers():
    content_type = "multipart/form-data; boundary=%s" % boundary
    return content_type


# function to return the fully formed url/end-point with the token ID
def url(text):
    return text + token


# function to return the fully formed url/end-point with the invalid token ID
def bad_url(text):
    return text + invalidToken


# function to return the fully formed url/end-point with the token ID for second user
def url_second(text):
    return text + second_user_token
