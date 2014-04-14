# Zed Attack Proxy (ZAP) and its related class files.
#
# ZAP is an HTTP/HTTPS proxy for assessing web application security.
#
# Copyright 2014 the ZAP development team
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
"""
This file was automatically generated.
"""

class brk(object):

    def __init__(self, zap):
        self.zap = zap

    def brk(self, type, scope, state, apikey=''):
        return self.zap._request(self.zap.base + 'break/action/break/', {'type' : type, 'scope' : scope, 'state' : state})

    def add_http_breakpoint(self, string, location, match, inverse, ignorecase, apikey=''):
        return self.zap._request(self.zap.base + 'break/action/addHttpBreakpoint/', {'string' : string, 'location' : location, 'match' : match, 'inverse' : inverse, 'ignorecase' : ignorecase})

    def remove_http_breakpoint(self, string, location, match, inverse, ignorecase, apikey=''):
        return self.zap._request(self.zap.base + 'break/action/removeHttpBreakpoint/', {'string' : string, 'location' : location, 'match' : match, 'inverse' : inverse, 'ignorecase' : ignorecase})


