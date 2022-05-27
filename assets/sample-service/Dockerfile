FROM golang:1.16-alpine

WORKDIR /app

COPY . .

COPY go.mod ./
COPY go.sum ./

RUN go mod download

COPY *.go ./
RUN ls

RUN go build -o example-service

EXPOSE 8199

CMD [ "./example-service" ]