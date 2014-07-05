<?php
/**
 * Zed Attack Proxy (ZAP) and its related class files.
 *
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 *
 * Copyright the ZAP development team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


namespace Zap;


/**
 * This file was automatically generated.
 */
class Users {

	public function __construct ($zap) {
		$this->zap = $zap;
	}

	public function usersList($contextid='') {
		return $this->zap->request($this->zap->base . 'users/view/usersList/', array('contextId' => $contextid))->{'usersList'};
	}

	public function getUserById($contextid='', $userid='') {
		return $this->zap->request($this->zap->base . 'users/view/getUserById/', array('contextId' => $contextid, 'userId' => $userid))->{'getUserById'};
	}

	public function getAuthenticationCredentialsConfigParams($contextid) {
		return $this->zap->request($this->zap->base . 'users/view/getAuthenticationCredentialsConfigParams/', array('contextId' => $contextid))->{'getAuthenticationCredentialsConfigParams'};
	}

	public function getAuthenticationCredentials($contextid, $userid) {
		return $this->zap->request($this->zap->base . 'users/view/getAuthenticationCredentials/', array('contextId' => $contextid, 'userId' => $userid))->{'getAuthenticationCredentials'};
	}

	public function newUser($contextid, $name, $apikey='') {
		return $this->zap->request($this->zap->base . 'users/action/newUser/', array('contextId' => $contextid, 'name' => $name, 'apikey' => $apikey));
	}

	public function removeUser($contextid, $userid, $apikey='') {
		return $this->zap->request($this->zap->base . 'users/action/removeUser/', array('contextId' => $contextid, 'userId' => $userid, 'apikey' => $apikey));
	}

	public function setUserEnabled($contextid, $userid, $enabled, $apikey='') {
		return $this->zap->request($this->zap->base . 'users/action/setUserEnabled/', array('contextId' => $contextid, 'userId' => $userid, 'enabled' => $enabled, 'apikey' => $apikey));
	}

	public function setUserName($contextid, $userid, $name, $apikey='') {
		return $this->zap->request($this->zap->base . 'users/action/setUserName/', array('contextId' => $contextid, 'userId' => $userid, 'name' => $name, 'apikey' => $apikey));
	}

	public function setAuthenticationCredentials($contextid, $userid, $authcredentialsconfigparams='', $apikey='') {
		return $this->zap->request($this->zap->base . 'users/action/setAuthenticationCredentials/', array('contextId' => $contextid, 'userId' => $userid, 'authCredentialsConfigParams' => $authcredentialsconfigparams, 'apikey' => $apikey));
	}

}
