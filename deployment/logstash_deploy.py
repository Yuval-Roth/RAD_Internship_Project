import sys
import os


def get_project_root():
    return cwd.rsplit("/", 1)[0]


def deploy_config(file_path):
    project_root = get_project_root()
    new_file = project_root + "/elk/logstash/config/logstash.conf"
    with open(file_path, 'r') as file:
        with open(new_file, 'w') as new:
            for line in file:
                new.write(line
                          .replace("{root}", project_root)
                          .replace("{elasticsearch_uri}", es_uri))


if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python logstash_deploy.py <elasticsearch_uri>")
        sys.exit(1)
    cwd = os.getcwd().replace("\\", "/")
    es_uri = sys.argv[1]
    deploy_config(cwd+"/logstash.conf.base")
