FROM python:3.9.16-alpine3.16
WORKDIR /service
COPY requirements.txt .
RUN python -m pip install -r requirements.txt
COPY . .
ENTRYPOINT ["python","main.py"]