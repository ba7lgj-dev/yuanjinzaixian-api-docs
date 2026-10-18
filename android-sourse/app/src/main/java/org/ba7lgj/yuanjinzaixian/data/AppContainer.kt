package org.ba7lgj.yuanjinzaixian.data

import android.content.Context

class AppContainer(context: Context) {
    private val appContext = context.applicationContext
    val database: AppDatabase = AppDatabase.get(appContext)
    val api: BiotApi = BiotApi()
    val credentials: CredentialStore = CredentialStore(appContext)
    val repository: BatteryRepository = BatteryRepository(
        context = appContext,
        database = database,
        api = api,
        credentials = credentials
    )
}
