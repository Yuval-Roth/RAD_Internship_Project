import sys
import os


def get_project_root():
    return cwd.rsplit("/", 1)[0]


def deploy_config(file_path):
    new_file = get_project_root() + "/elk/kibana/config/kibana.yml"
    with open(file_path, 'r') as file:
        with open(new_file, 'w') as new:
            for line in file:
                if line.startswith("#"):
                    new.write(line)
                else:
                    new.write(line
                              .replace("{elasticsearch_uri}", es_uri)
                              .replace("{kibana_port}", kibana_port))


if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python kibana_deploy.py <kibana_port> <elasticsearch_uri>")
        sys.exit(1)
    cwd = os.getcwd().replace("\\", "/")
    kibana_port = sys.argv[1]
    es_uri = sys.argv[2]
    deploy_config(cwd + "/kibana.yml.base")
