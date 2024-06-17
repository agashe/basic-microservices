import Fastify from 'fastify';
import StudentsService from './services/StudentsService.mjs';

const server = Fastify({
  logger: true
});

const studentsService = new StudentsService();

server.register(function (app, _, done) {
  app.get('/', function (request, reply) {
    studentsService.all(function (message) {
      reply.send({ data: JSON.parse(message) });
    });
  });

  app.get('/:id', function (request, reply) {
    const { id } = request.params;

    studentsService.find(id, function (message) {
      reply.send({ data: JSON.parse(message) });
    });
  });

  app.post('/', function (request, reply) {
    studentsService.create(request.body, function (message) {
      reply.send({ data: JSON.parse(message) });
    });
  });

  app.put('/:id', function (request, reply) {
    const { id } = request.params;

    studentsService.update(id, request.body, function (message) {
      reply.statusCode = 204;
      reply.send();
    });
  });
  
  app.delete('/:id', function (request, reply) {
    const { id } = request.params;

    studentsService.delete(id, function (message) {
      reply.statusCode = 204;
      reply.send();
    });
  });

  done();
}, { prefix: '/api/v1/students' });

server.listen({ port: 3000 }, function (err, address) {
  if (err) {
    server.log.error(err);
    process.exit(1);
  }
}); 
