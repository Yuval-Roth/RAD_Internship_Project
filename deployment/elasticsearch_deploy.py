import platform
import sys
import os


def get_project_root():
    return cwd.rsplit("/", 1)[0]


def deploy_config(file_path):
    new_file = get_project_root() + "/elk/elasticsearch/config/elasticsearch.yml"
    host_name = platform.uname().node
    with open(file_path, 'r') as file:
        with open(new_file, 'w') as new:
            for line in file:
                if line.startswith("#"):
                    new.write(line)
                else:
                    new.write(line
                              .replace("{host_name}", host_name)
                              .replace("{network_host}", network_host)
                              .replace("{http_port}", http_port))


if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python elasticsearch_deploy.py <network_host> <http_port>")
        sys.exit(1)
    cwd = os.getcwd().replace("\\", "/")
    network_host = sys.argv[1]
    http_port = sys.argv[2]
    deploy_config(cwd + "/elasticsearch.yml.base")
