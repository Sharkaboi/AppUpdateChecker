package com.sharkaboi.appupdatechecker.models

sealed class AppUpdateExceptions(message: String) : Exception(message)

class InvalidVersionException(message: String) : AppUpdateExceptions(message)

class InvalidPackageNameException(message: String) : AppUpdateExceptions(message)

class PackageNotFoundException(message: String) : AppUpdateExceptions(message)

class InvalidUserNameException(message: String) : AppUpdateExceptions(message)

class InvalidRepositoryNameException(message: String) : AppUpdateExceptions(message)

class InvalidEndPointException(message: String) : AppUpdateExceptions(message)