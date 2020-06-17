from share_box import common
import json

URI = '/api/files'


def get_all_files(get_shared_files=False):
    """Hit the sharebox url and fetch all the files linked with auth token."""
    params = {}
    if get_shared_files:
        params['getSharedFiles'] = True

    resp = common.hit_url(URI, params)
    if resp:
        resp_data, resp_code = resp
        if resp_code == 200:
            return resp_data
        elif resp_code == 401:
            return "Authentication failure"
        elif resp_code == 403:
            return "Access forbidden"
        elif resp_code == 500:
            return "Internal server error."


def delete_file(file_id):
    """Delete the file from ShareBox.

    :parameter
    file_id (str): file to be deleted.

    :return
    str: success or failure message.
    """
    params = {'fileId': file_id}
    headers = {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }
    resp = common.hit_url(URI, json.dumps(params).encode(), headers, method_type='DELETE')
    if resp:
        resp_data, resp_code = resp
        if resp_code == 200:
            return resp_data
        elif resp_code == 401:
            return "Authentication failure"


def share_file(file_id, share_to):
    """Share file to the user.

    :parameter
    file_id (str): file to be shared
    share_to (str): Name of the user with which file needs to be shared.

    :return
    str: success or failure message based on status
    """
    params = {'fileId': file_id,
              'shareTo': share_to}

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


def resp_share_file(file_id, is_accepted):
    """Accept/reject shared file.

    :parameter
    file_id (str): file to be shared
    is_accepted (boot): True to accept and False to reject.

    :return
    str: success or failure message based on status
    """
    params = {'fileId': file_id,
              'isAccepted': is_accepted}

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
