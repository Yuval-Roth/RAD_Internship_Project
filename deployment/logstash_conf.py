import os


def get_cwd():
    return os.getcwd().replace("\\", "/")


def get_project_root():
    return get_cwd().split("/deployment")[0]


def deploy_config(file_path):
    project_root = get_project_root()
    new_file = project_root + "/elk/logstash/config/logstash.conf"
    with open(file_path, 'r') as file:
        with open(new_file, 'w') as new:
            for line in file:
                new.write(line.replace("{root}", project_root))


deploy_config(get_cwd()+"/logstash.conf.base")
