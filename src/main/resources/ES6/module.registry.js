/**
 * Class to register function files.
 */
class ModuleRegistry {
    registryMap;

    constructor() {
        this.registryMap = new Map();
    }

    register(name, script) {
        if (!this.registryMap.has(name)) {
            this.registryMap.set(name, script);
        }
    }

    get(name) {
        // return pre-executed function
        return (this.registryMap.get(name))();
    }
}
