#include "msg.pb-c.h"
#include <stdio.h>
#include <stdlib.h>

#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <string.h>

// 192.168.39.129 10905
// ssh unstatik@172.18.19.19
// ssh -R 10905:127.0.0.1:10905 unstatik@192.168.39.134

Packet generate_packet(int id, char *name);
unsigned char* pack(Wrapper wrapper);
size_t get_msg_size(unsigned char *in);
void print_message(Wrapper *input);
void read_bytes(int socket, unsigned int x, void* buffer);

int main()
{
  Packet first_packet = generate_packet(1, "first");
  Packet second_packet = generate_packet(2, "second");

  Packet *packets[2];
  packets[0] = &first_packet;
  packets[1] = &second_packet;

  Wrapper wrapper = WRAPPER__INIT;
  wrapper.tag = "test_tag";
  wrapper.n_packets = 2;
  wrapper.packets = packets;
  unsigned char *in = pack(wrapper);

  unsigned char *recvBuff;
  int sockfd, portno, n;
  struct sockaddr_in serv_addr;
  struct hostent *server;

  sockfd = socket(AF_INET, SOCK_STREAM, 0);
  memset(&serv_addr, '0', sizeof(serv_addr));
  serv_addr.sin_family = AF_INET;
  serv_addr.sin_port = htons(10905);
  inet_pton(AF_INET, "127.0.0.1", &serv_addr.sin_addr);
  int status = connect(sockfd, (struct sockaddr *)&serv_addr, sizeof(serv_addr));
  printf("connection status: %d\n", status);

  write(sockfd, "hello", strlen("hello"));
  int rc;
  char buf[120];
  for (;;)
  {
    rc = recv(sockfd, buf, sizeof(buf), 0);
    if (rc <= 0) {
       break;
    }
  }
  recvBuff = malloc(sizeof(buf));
  strcpy(recvBuff, buf);
  size_t rc_len = get_msg_size(recvBuff);
  printf("receive bytes: %d\n", rc_len);
  Wrapper *input = wrapper__unpack (NULL, rc_len, recvBuff);
  print_message(input);


//  size_t msg_len = get_msg_size(in);
//  Wrapper *input = wrapper__unpack (NULL, msg_len, in);
//  print_message(input);
  return 0;
}

void read_bytes(int socket, unsigned int x, void* buffer) {
    int bytesRead = 0;
    int result;
    while (bytesRead < x)
    {
        result = read(socket, buffer + bytesRead, x - bytesRead);
        if (result < 1 )
        {
            // Throw your error.
        }

        bytesRead += result;
    }
   printf("receive bytes: %d\n", result);
}

void print_message(Wrapper *input) {
  printf("tag: %s\n", input->tag);
  printf("count packets: %lu\n", input->n_packets);
  for(int n=0; n<input->n_packets; n++) {
    printf("packet id: %d\n", input->packets[n]->id);
    printf("packet name: %s\n", input->packets[n]->name);
  }
}

unsigned char* pack(Wrapper wrapper) {
  unsigned char *packed;
  size_t size = wrapper__get_packed_size(&wrapper);
  packed = malloc(size);
  wrapper__pack(&wrapper, packed);
  return packed;
}

Packet generate_packet(int id, char *name) {
  Packet packet = PACKET__INIT;
  packet.id = id;
  packet.name = name;
  return packet;
}

size_t get_msg_size(unsigned char *in) {
  size_t index = 0;
  while(in[index] != '\0') {
    ++index;
  }
  return index;
}
