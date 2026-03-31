#!/system/bin/sh
lang_version_help="Usage: version ([Args]) ([runtime])\n\
Get the version of the runtime.\n\
[Args]\n\
    -h, --help\n\
        display this help and exit\n\
    -n\n\
        do not output the trailing newline\n\
        (should only be used in .sh script, does NOT support output in FCL Shell!)\n\
[runtime]\n\
        Available component: Component names listed when run without a specificd component.\n\
        when empty: list all components and their version\n\
        when not empty: get the version of the specific component"
lang_version_notfound="component not found."