Running class info.kgeorgiy.java.advanced.walk.WalkTest for info.kgeorgiy.ja.mustafina.walk.Walk
=== Running test10_oneEmptyFile
=== Running test15_tenEmptyFiles
=== Running test20_smallRandomFiles
=== Running test21_mediumRandomFiles
=== Running test22_largeRandomFiles
=== Running test23_veryLargeFile
=== Running test30_missingFiles
=== Running test40_errorReading
=== Running test45_partiallyMissingFiles
=== Running test46_filesAndDirs
=== Running test50_whitespaceSupport
=== Running test55_chineseSupport
=== Running test60_noInput
Failed to find input file
=== Running test61_invalidInput
Failed to find input file
Invalid path
=== Running test62_invalidOutput
Failed to find input file
Invalid path
Failed to find input file
=== Running test63_invalidFiles
=== Running test70_singleArgument
Invalid number of arguments
=== Running test71_noArguments
Invalid number of arguments
=== Running test72_nullArguments
Invalid number of arguments
=== Running test73_firstArgumentNull
Invalid number of arguments
=== Running test74_secondArgumentNull
Invalid number of arguments
=== Running test75_threeArguments
Invalid number of arguments
Test test30_missingFiles failed: Error thrown
java.lang.AssertionError: Error thrown
at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.runRaw(WalkTest.java:276)
at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.run(WalkTest.java:260)
at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.test(WalkTest.java:244)
at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.test(WalkTest.java:233)
at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.test30_missingFiles(WalkTest.java:109)
at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
at java.base/java.lang.reflect.Method.invoke(Method.java:568)
at junit@4.11/org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:47)
at junit@4.11/org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
at junit@4.11/org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:44)
at junit@4.11/org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
at junit@4.11/org.junit.rules.TestWatcher$1.evaluate(TestWatcher.java:55)
at junit@4.11/org.junit.rules.RunRules.evaluate(RunRules.java:20)
at junit@4.11/org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:271)
at junit@4.11/org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:70)
at junit@4.11/org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:50)
at junit@4.11/org.junit.runners.ParentRunner$3.run(ParentRunner.java:238)
at junit@4.11/org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:63)
at junit@4.11/org.junit.runners.ParentRunner.runChildren(ParentRunner.java:236)
at junit@4.11/org.junit.runners.ParentRunner.access$000(ParentRunner.java:53)
at junit@4.11/org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:229)
at junit@4.11/org.junit.internal.runners.statements.RunBefores.evaluate(RunBefores.java:26)
at junit@4.11/org.junit.runners.ParentRunner.run(ParentRunner.java:309)
at junit@4.11/org.junit.runners.Suite.runChild(Suite.java:127)
at junit@4.11/org.junit.runners.Suite.runChild(Suite.java:26)
at junit@4.11/org.junit.runners.ParentRunner$3.run(ParentRunner.java:238)
at junit@4.11/org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:63)
at junit@4.11/org.junit.runners.ParentRunner.runChildren(ParentRunner.java:236)
at junit@4.11/org.junit.runners.ParentRunner.access$000(ParentRunner.java:53)
at junit@4.11/org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:229)
at junit@4.11/org.junit.runners.ParentRunner.run(ParentRunner.java:309)
at junit@4.11/org.junit.runner.JUnitCore.run(JUnitCore.java:160)
at junit@4.11/org.junit.runner.JUnitCore.run(JUnitCore.java:138)
at junit@4.11/org.junit.runner.JUnitCore.run(JUnitCore.java:117)
at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.test(BaseTester.java:55)
at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.lambda$add$0(BaseTester.java:95)
at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.test(BaseTester.java:48)
at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.run(BaseTester.java:39)
at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.Tester.main(Tester.java:24)
Caused by: java.security.AccessControlException: access denied ("java.io.FilePermission" "\\.." "read")
at java.base/java.security.AccessControlContext.checkPermission(AccessControlContext.java:485)
at java.base/java.security.AccessController.checkPermission(AccessController.java:1068)
at java.base/java.lang.SecurityManager.checkPermission(SecurityManager.java:416)
at java.base/java.lang.SecurityManager.checkRead(SecurityManager.java:756)
at java.base/java.io.File.isFile(File.java:893)
at info.kgeorgiy.ja.mustafina.walk.Walk.main(Walk.java:53)
at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
at java.base/java.lang.reflect.Method.invoke(Method.java:568)
at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.runRaw(WalkTest.java:272)
... 40 more
Test test50_whitespaceSupport failed: Error thrown
java.lang.AssertionError: Error thrown
at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.runRaw(WalkTest.java:276)
at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.run(WalkTest.java:260)
at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.test(WalkTest.java:244)
at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.test(WalkTest.java:233)
at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.testAlphabet(WalkTest.java:151)
at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.test50_whitespaceSupport(WalkTest.java:141)
at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
at java.base/java.lang.reflect.Method.invoke(Method.java:568)
at junit@4.11/org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:47)
at junit@4.11/org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
at junit@4.11/org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:44)
at junit@4.11/org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
at junit@4.11/org.junit.rules.TestWatcher$1.evaluate(TestWatcher.java:55)
at junit@4.11/org.junit.rules.RunRules.evaluate(RunRules.java:20)
at junit@4.11/org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:271)
at junit@4.11/org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:70)
at junit@4.11/org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:50)
at junit@4.11/org.junit.runners.ParentRunner$3.run(ParentRunner.java:238)
at junit@4.11/org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:63)
at junit@4.11/org.junit.runners.ParentRunner.runChildren(ParentRunner.java:236)
at junit@4.11/org.junit.runners.ParentRunner.access$000(ParentRunner.java:53)
at junit@4.11/org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:229)
at junit@4.11/org.junit.internal.runners.statements.RunBefores.evaluate(RunBefores.java:26)
at junit@4.11/org.junit.runners.ParentRunner.run(ParentRunner.java:309)
at junit@4.11/org.junit.runners.Suite.runChild(Suite.java:127)
at junit@4.11/org.junit.runners.Suite.runChild(Suite.java:26)
at junit@4.11/org.junit.runners.ParentRunner$3.run(ParentRunner.java:238)
at junit@4.11/org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:63)
at junit@4.11/org.junit.runners.ParentRunner.runChildren(ParentRunner.java:236)
at junit@4.11/org.junit.runners.ParentRunner.access$000(ParentRunner.java:53)
at junit@4.11/org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:229)
at junit@4.11/org.junit.runners.ParentRunner.run(ParentRunner.java:309)
at junit@4.11/org.junit.runner.JUnitCore.run(JUnitCore.java:160)
at junit@4.11/org.junit.runner.JUnitCore.run(JUnitCore.java:138)
at junit@4.11/org.junit.runner.JUnitCore.run(JUnitCore.java:117)
at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.test(BaseTester.java:55)
at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.lambda$add$0(BaseTester.java:95)
at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.test(BaseTester.java:48)
at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.run(BaseTester.java:39)
at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.Tester.main(Tester.java:24)
Caused by: java.security.AccessControlException: access denied ("java.io.FilePermission" "__Test__Walk__\test50_whitespaceSupport\  _ � �_ �� �_�� � _��    � _ " "read")
at java.base/java.security.AccessControlContext.checkPermission(AccessControlContext.java:485)
at java.base/java.security.AccessController.checkPermission(AccessController.java:1068)
at java.base/java.lang.SecurityManager.checkPermission(SecurityManager.java:416)
at java.base/java.lang.SecurityManager.checkRead(SecurityManager.java:756)
at java.base/java.io.File.isFile(File.java:893)
at info.kgeorgiy.ja.mustafina.walk.Walk.main(Walk.java:53)
at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
at java.base/java.lang.reflect.Method.invoke(Method.java:568)
at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.runRaw(WalkTest.java:272)
... 41 more
Test test63_invalidFiles failed: Error thrown
java.lang.AssertionError: Error thrown
at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.runRaw(WalkTest.java:276)
at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.run(WalkTest.java:260)
at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.test(WalkTest.java:244)
at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.test(WalkTest.java:233)
at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.testAlphabet(WalkTest.java:151)
at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.test63_invalidFiles(WalkTest.java:177)
at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
at java.base/java.lang.reflect.Method.invoke(Method.java:568)
at junit@4.11/org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:47)
at junit@4.11/org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
at junit@4.11/org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:44)
at junit@4.11/org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
at junit@4.11/org.junit.rules.TestWatcher$1.evaluate(TestWatcher.java:55)
at junit@4.11/org.junit.rules.RunRules.evaluate(RunRules.java:20)
at junit@4.11/org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:271)
at junit@4.11/org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:70)
at junit@4.11/org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:50)
at junit@4.11/org.junit.runners.ParentRunner$3.run(ParentRunner.java:238)
at junit@4.11/org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:63)
at junit@4.11/org.junit.runners.ParentRunner.runChildren(ParentRunner.java:236)
at junit@4.11/org.junit.runners.ParentRunner.access$000(ParentRunner.java:53)
at junit@4.11/org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:229)
at junit@4.11/org.junit.internal.runners.statements.RunBefores.evaluate(RunBefores.java:26)
at junit@4.11/org.junit.runners.ParentRunner.run(ParentRunner.java:309)
at junit@4.11/org.junit.runners.Suite.runChild(Suite.java:127)
at junit@4.11/org.junit.runners.Suite.runChild(Suite.java:26)
at junit@4.11/org.junit.runners.ParentRunner$3.run(ParentRunner.java:238)
at junit@4.11/org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:63)
at junit@4.11/org.junit.runners.ParentRunner.runChildren(ParentRunner.java:236)
at junit@4.11/org.junit.runners.ParentRunner.access$000(ParentRunner.java:53)
at junit@4.11/org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:229)
at junit@4.11/org.junit.runners.ParentRunner.run(ParentRunner.java:309)
at junit@4.11/org.junit.runner.JUnitCore.run(JUnitCore.java:160)
at junit@4.11/org.junit.runner.JUnitCore.run(JUnitCore.java:138)
at junit@4.11/org.junit.runner.JUnitCore.run(JUnitCore.java:117)
at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.test(BaseTester.java:55)
at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.lambda$add$0(BaseTester.java:95)
at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.test(BaseTester.java:48)
at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.run(BaseTester.java:39)
at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.Tester.main(Tester.java:24)
Caused by: java.security.AccessControlException: access denied ("java.io.FilePermission" "__Test__Walk__\test63_invalidFiles\ \  * \ \*\ \ ***\** *\*" "read")
at java.base/java.security.AccessControlContext.checkPermission(AccessControlContext.java:485)
at java.base/java.security.AccessController.checkPermission(AccessController.java:1068)
at java.base/java.lang.SecurityManager.checkPermission(SecurityManager.java:416)
at java.base/java.lang.SecurityManager.checkRead(SecurityManager.java:756)
at java.base/java.io.File.isFile(File.java:893)
at info.kgeorgiy.ja.mustafina.walk.Walk.main(Walk.java:53)
at jdk.internal.reflect.GeneratedMethodAccessor1.invoke(Unknown Source)
at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
at java.base/java.lang.reflect.Method.invoke(Method.java:568)
at info.kgeorgiy.java.advanced.walk/info.kgeorgiy.java.advanced.walk.WalkTest.runRaw(WalkTest.java:272)
... 41 more
