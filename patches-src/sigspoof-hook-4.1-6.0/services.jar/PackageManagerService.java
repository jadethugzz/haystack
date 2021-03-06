/*
 * Copyright (C) 2016 Lanchon <https://github.com/Lanchon>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.server.pm;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageParser;

import lanchon.dexpatcher.annotation.*;

// Lets modify the PackageManagerService class.
@DexEdit(onlyEditMembers = true)
public class PackageManagerService /* extends IPackageManager.Stub */ {

    // We need to declare this field to be able to reference it from the patch.
    // The field itself provided by the patch will be discarded.
    @DexIgnore
    final Context mContext;

    // If we do not define any constructors the compiler will add one by default,
    // so lets define one and instruct the patcher to discard it.
    @DexIgnore
    private PackageManagerService() { throw null; }

    // Rename the existing generatePackageInfo(...) method. The stub method
    // implementation provided by the patch will be discarded.
    @DexEdit(target = "generatePackageInfo")
    PackageInfo source_Hook_generatePackageInfo(PackageParser.Package p, int flags, int userId) { throw null; }
    // Add a new generatePackageInfo(...) method. Call the original method we
    // are replacing, then invoke a hook that can modify its return value.
    @DexAdd
    PackageInfo generatePackageInfo(PackageParser.Package p, int flags, int userId) {
        PackageInfo pi = source_Hook_generatePackageInfo(p, flags, userId);
        if (p != null && pi != null) pi = GeneratePackageInfoHook.hook(pi, mContext, p, flags, userId);
        return pi;
    }

}
