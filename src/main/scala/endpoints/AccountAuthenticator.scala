package io.blindnet.identity
package endpoints

import models.Account

import io.blindnet.identityclient.auth.StAuthenticator

type AccountAuthenticator = StAuthenticator[Account, Account]
