"""This module is to take care of common functionality and constants."""

from urllib import request, parse
import ssl
ssl._create_default_https_context = ssl._create_unverified_context

URL = 'https://ec2-35-154-146-139.ap-south-1.compute.amazonaws.com/sharebox'
AUTH_KEY = '3b1d38df-9d14-40ff-84be-be6ce9a153cd'


def hit_url(uri, data=None, headers=None, method_type='GET'):
    """This function will hit the url with the data provided.

    :parameter
    url (str): url to be hit
    data (dict; Optional): Parameters to be passed to url
    headers (dict; Optional): Headers to be passed to url
    method_type (str; Optional): HTTP method to be hit by URL

    :return
    str: response from the url.
    """
    try:
        #url = f'{URL + uri}?token={AUTH_KEY}'
        if method_type == 'GET':
            url=URL+uri+'&token='+AUTH_KEY
        else:
            url=URL+uri+'?token='+AUTH_KEY
        data = {} if not data else data
        headers = {} if not headers else headers
        req = request.Request(url, data, headers=headers, method=method_type)
        resp = request.urlopen(req)
        resp_data = resp.read()
        resp_code = resp.getcode()
    
    except Exception as e:
        print(f"Error while hitting the url: {url} with data: {data}")
        print(e)
    else:
        return resp_data, resp_code
