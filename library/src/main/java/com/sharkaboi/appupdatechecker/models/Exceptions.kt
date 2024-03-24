package com.sharkaboi.appupdatechecker.models

sealed interface AppUpdateCheckerException

class InvalidVersionException(message: String) : Exception(message), AppUpdateCheckerException

class InvalidPackageNameException(message: String) : Exception(message), AppUpdateCheckerException

class PackageNotFoundException(message: String) : Exception(message), AppUpdateCheckerException

class InvalidUserNameException(message: String) : Exception(message), AppUpdateCheckerException

class InvalidRepositoryNameException(message: String) :
    Exception(message),
    AppUpdateCheckerException

class InvalidEndPointException(message: String) : Exception(message), AppUpdateCheckerException

class RemoteError(throwable: Throwable) : Exception(throwable), AppUpdateCheckerException

class GenericError(throwable: Throwable) : Exception(throwable), AppUpdateCheckerException
