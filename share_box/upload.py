from share_box import common
import json

URI = '/api/upload'


def get_upload_file_details(file_id):
    """get details of Uploaded file to ShareBox."""
    params = {'fileId': file_id}
    resp = common.hit_url(URI, params)
    if resp:
        resp_data, resp_code = resp
        if resp_code == 200:
            return resp_data
        elif resp_code == 400:
            return "Bad Request"
        elif resp_code == 401:
            return "Authentication failure"
        elif resp_code == 403:
            return "Access forbidden"
        elif resp_code == 500:
            return "Internal server error."


def upload_file(file_name, file_size, file_hash, file_):
    """Delete the file from ShareBox.

    :parameter
    file_name (str): name of the file
    file_size (str): file size
    file_hash (str): file hash
    file_ (str): file handler

    :return
    str: success or failure message.
    """
    params = {'name': file_name,
              'size': file_size,
              'hash': file_hash,
              'file': str(file_)}
    headers = {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }
    resp = common.hit_url(URI, json.dumps(params).encode(), headers, method_type='POST')
    if resp:
        resp_data, resp_code = resp
        if resp_code == 200:
            return resp_data
        elif resp_code == 400:
            return "Bad Request"
        elif resp_code == 401:
            return "Authentication failure"
        elif resp_code == 403:
            return "Access forbidden"
        elif resp_code == 500:
            return "Internal server error."


def update_file_status(file_id):
    """Update the upload file status.

    :parameter
    file_id (str): file to be shared
    bytes_completed (str): bytes completed

    :return
    str: success or failure message based on status
    """
    params = {'fileId': file_id,
              'bytesCompleted': '100'}

    headers = {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }

    resp = common.hit_url(URI, json.dumps(params).encode(), headers, method_type='PUT')
    if resp:
        resp_data, resp_code = resp
        if resp_code == 200:
            return resp_data
        elif resp_code == 400:
            return "Bad Request"
        elif resp_code == 401:
            return "Authentication failure"
        elif resp_code == 403:
            return "Access forbidden"
        elif resp_code == 404:
            return "File not found"
        elif resp_code == 500:
            return "Internal server error."
