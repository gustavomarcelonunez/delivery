const assert = require('assert');
const { Given, When, Then } = require('cucumber');
const request = require('sync-request');
const jsondiff = require('json-diff');

const url = 'http://server:8080/labprog-server/rest';
const deleteKey = require('key-del');

let pedido = {};

Given('que llama el cliente existente con razón social {string} con cuit {string}', function (razonSocial, cuit) {
    try {
        let res = request('GET', `${url}/clientes/${cuit}`);

        let body = JSON.parse(res.getBody('utf8'));

        if (body.StatusCode == 200) {
            assert.equal(razonSocial, body.data.razonSocial);
            assert.equal(cuit, body.data.cuit);
            //va generando el cliente

            pedido.cliente = body.data;
            return true;
        } else {

            return assert.fail(body.StatusText);
        }
    } catch (error) {
        return assert.fail(error.message);
    }
});

Given('que solicita se envíe el pedido al domicilio existente en la localidad {string} calle {string} y altura {int}', function (localidad, calle, altura) {
    try {
        let res = request('GET', `${url}/clientes/${pedido.cliente.cuit}/domicilios`);

        let body = JSON.parse(res.getBody('utf8'));

        if (body.StatusCode == 200) {

            // busca el domicilio en la lista
            for (let i = 0; i < body.data.domicilios.length; i++) {
                let domicilio = body.data.domicilios[i];
                if (domicilio.localidad.nombre == localidad
                    && domicilio.calle == calle
                    && domicilio.altura == altura
                ) {
                    pedido.domicilioEntrega = domicilio;
                    return true;
                }
            }
            return assert.fail('Domicilio del cliente no existe');
        } else {
            return assert.fail(body.StatusText);
        }
    } catch (error) {
        return assert.fail(error.message);
    }
});

Given('que pide los siguientes artículos', function (articulosPedidosDT) {

    let articulosPedido = articulosPedidosDT.hashes();

    pedido.articulosPedido = [];
    let result = articulosPedido
        .map((aArticulo) => {
            try {
                let res = request('GET', `${url}/articulos/${aArticulo.articulo}`);
                let body = JSON.parse(res.getBody('utf8'));

                if (body.StatusCode == 200) {
                    return true;
                }
            } catch (error) {
                return assert.fail(error.message);
            }
            return false;
        })
        .reduce((previous, current) => {
            return previous && current;
        }, true);

    if (result == false) {
        return assert.fail("No existen todos los articulos");
    }

    for (let i = 0; i < articulosPedido.length; i++) {

        pedido.articulosPedido.push({
            articulo: { codigo: articulosPedido[i].articulo },
            cantidad: articulosPedido[i].cantidad,
            precioUnitario: articulosPedido[i].precioUnitario
        }
        );
    }

});

let response;

When('guarda el nuevo pedido con fecha {string}', function (fechaPedido) {

    pedido.date = fechaPedido;
    let res;
    try {
        res = request('POST', `${url}/pedidos`, { json: pedido });
        response = JSON.parse(res.getBody('utf8'));
        response = deleteKey(response, "id");
        response = deleteKey(response, "pedido");
        response = deleteKey(response, "cliente");
        response = deleteKey(response, "domicilio");
        response = deleteKey(response, "localidad");

        if (response.StatusCode == 200) {
            return true;
        } if (response.StatusCode == 400) {
            return true;
        } else {
            return assert.fail(response.StatusText);
        }
    } catch (error) {
        return assert.fail(error.message);
    }
});

Then('obtiene la siguiente respuesta {string}', function (resultadoEsperado) {

    let json = JSON.parse(resultadoEsperado);

    json = deleteKey(json, "id");
    json = deleteKey(json, "pedido");
    json = deleteKey(json, "cliente");
    json = deleteKey(json, "domicilio");
    json = deleteKey(json, "localidad");

    return assert.equal(undefined, jsondiff.diff(json, response));
});

Given('que llama la el cliente que no existe con razón social {string} con cuit {string}', function (razonSocial, cuit) {
    // Write code here that turns the phrase above into concrete actions
    try {
        let res = request('GET', `${url}/clientes/${cuit}`);

        let body = JSON.parse(res.getBody('utf8'));

        if (body.StatusCode == 501) {
            pedido.cliente = {};
            pedido.cliente.razonSocial = razonSocial;
            pedido.cliente.cuit = cuit;
            return assert.ok(body.StatusText);
        } else {
            return assert.fail(body.StatusText);
        }
    } catch (error) {

        return assert.fail(error.message);
    }
});

Given('que solicita se envíe el pedido al domicilio que no existe con localidad {string} calle {string} y altura {int}', function (localidad, calle, altura) {
    // Write code here that turns the phrase above into concrete actions
    //Hay que modificar esta verificación

    try {
        let res = request('GET', `${url}/clientes/${pedido.cliente.cuit}/domicilios`);

        let body = JSON.parse(res.getBody('utf8'));

        if (body.StatusCode == 500) {
            pedido.domicilioEntrega = {};
            pedido.domicilioEntrega.localidad = {};
            pedido.domicilioEntrega.calle = calle;
            pedido.domicilioEntrega.altura = altura;
            pedido.domicilioEntrega.localidad.nombre = localidad;
            return assert.ok(body.StatusText);
        } else {
            return assert.fail(body.StatusText);
        }
    } catch (error) {
        return assert.fail(error.message);
    }

});
