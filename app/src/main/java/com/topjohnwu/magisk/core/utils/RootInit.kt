package com.topjohnwu.magisk.core.utils

import android.content.Context
import com.topjohnwu.magisk.R
import com.topjohnwu.magisk.core.Const
import com.topjohnwu.magisk.core.wrap
import com.topjohnwu.magisk.extensions.rawResource
import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.io.SuFile

class RootInit : Shell.Initializer() {

    override fun onInit(context: Context, shell: Shell): Boolean {
        return init(context.wrap(), shell)
    }

    fun init(context: Context, shell: Shell): Boolean {
        val job = shell.newJob()
        job.add(context.rawResource(R.raw.manager))
        if (shell.isRoot) {
            job.add(context.rawResource(R.raw.util_functions))
                .add("SHA1=`grep_prop SHA1 /sbin/.magisk/config`")
            Const.MAGISK_DISABLE_FILE = SuFile("/cache/.disable_magisk")
        }

        job.add(
            "mount_partitions",
            "get_flags",
            "run_migrations",
            "export BOOTMODE=true"
        ).exec()

        return true
    }
}
