#include <android/sensor.h>

namespace {
// Keep libsensorndkbridge in DT_NEEDED even with --as-needed and section GC.
// Legacy blobs resolve the NDK APIs through this dependency, so do not define
// ASensor*/ALooper* stubs here: they would shadow the real implementations.
__attribute__((used, retain)) const auto sensor_bridge_dependency =
        &ASensorManager_getInstanceForPackage;
}

namespace android {
    // NDK sensor and looper symbols come from the libsensorndkbridge dependency.
    // libandroid_runtime.so
    extern "C" void _ZNK7android6Sensor11getMaxValueEv() {}
}
