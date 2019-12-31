import json
from objectifier import Objectifier
import requests as rq
import os

test_configs = Objectifier(json.load(open('testconfig.json')))


def upload_a_file(file, token=test_configs.token):
    rs = rq.post(url=test_configs.SHAREBOX_URL + '/upload?token=' + test_configs.token, json=file, verify=False)
    return rs


def get_upload_status(file_id, token=test_configs.token):
    rs = rq.get(url=test_configs.SHAREBOX_URL + '/upload?token=' + test_configs.token + "&fileId=" + file_id,
                verify=False)
    return rs


def update_upload_status(data, token=test_configs.token):
    rs = rq.put(url=test_configs.SHAREBOX_URL + '/upload?token=' + test_configs.token, data=data, verify=False)
    return rs


def get_list_of_files_for_token(token=test_configs.token, method='GET'):
    if (method == 'GET'):
        rs = rq.get(url=test_configs.SHAREBOX_URL + '/files?token=' + test_configs.token, verify=False)
        return rs
    else:
        rs = rq.post(url=test_configs.SHAREBOX_URL + '/files?token=' + test_configs.token, verify=False)
        return rs


def delete_a_file(data, token=test_configs.token):
    rs = rq.delete(url=test_configs.SHAREBOX_URL + '/files?token=' + test_configs.token, data=data, verify=False)
    return rs


def share_a_file(data, token=test_configs.token):
    rs = rq.post(url=test_configs.SHAREBOX_URL + '/files?token=' + test_configs.token, data=data, verify=False)
    return rs


def get_token_by_session():
    cookies = {
        'session_id_sharebox': '171.78.173.132-bfa1076e-d6e8-403a-a5c9-00e05785185d',
    }

    headers = {
        'Connection': 'keep-alive',
        'Pragma': 'no-cache',
        'Cache-Control': 'no-cache',
        'Accept': 'application/json, text/javascript, */*; q=0.01',
        'X-Requested-With': 'XMLHttpRequest',
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36',
        'Sec-Fetch-Site': 'same-origin',
        'Sec-Fetch-Mode': 'cors',
        'Referer': 'https://ec2-13-232-224-131.ap-south-1.compute.amazonaws.com/sharebox/token/generate_token',
        'Accept-Encoding': 'gzip, deflate, br',
        'Accept-Language': 'en-US,en;q=0.9,ta-IN;q=0.8,ta;q=0.7',
    }

    response = rq.get('https://ec2-13-232-224-131.ap-south-1.compute.amazonaws.com/sharebox/api/token', headers=headers,
                      cookies=cookies, verify=False)
    if (response.status_code == 200):
        token_data = json.loads(response.text)
        token_data = json.loads(token_data)
        token = token_data[0]['token']
        config_data = json.load(open('testconfig.json'))
        config_data['token'] = token
        json.dump(config_data, open('testconfig.json', 'w+'))
        global test_configs
        test_configs = Objectifier(json.load(open('testconfig.json')))

        return True
    return False