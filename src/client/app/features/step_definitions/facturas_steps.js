const assert = require('assert');
const { Given, When, Then } = require('cucumber');
const request = require('sync-request');
const jsondiff = require('json-diff');

const url = 'http://server:8080/labprog-server/rest';
const deleteKey = require('key-del');

let newRemito = {};
Given('el remito {int} que no está entregado aún', function (remito) {
    // Write code here that turns the phrase above into concrete actions
    try {
        let res = request('GET', `${url}/remitos/${remito}`);
        let body = JSON.parse(res.getBody('utf8'));
        if (body.StatusCode == 200) {
            if (body.data.entregado == false) {
                newRemito.id = remito;
                return true;
            } else {
                return assert.fail("El remito " + remito + " ya ha sido entregado.");
            }
        } else {
            return assert.fail(body.StatusText);
        }
    } catch (error) {
        return assert.fail(error.message);
    }

});


When('se marca como entregado al remito número {int}', function (remito) {
    // Write code here that turns the phrase above into concrete actions
    try {
        const res = request('PUT', `${url}/remitos`, {
            json: newRemito
        });
        response = JSON.parse(res.getBody('utf8'));
        return true;

    } catch (error) {
        return assert.fail(error.message);
    }
});

Then('se marca como entregado se obtiene {string}', function (resultadoEsperado) {
    // Write code here that turns the phrase above into concrete actions
    let json = JSON.parse(resultadoEsperado);
    response = deleteKey(response, "domicilioEntrega");
    response = deleteKey(response, "entregado");
    response = deleteKey(response, "fechaArmado");

    return assert.equal(undefined, jsondiff.diff(json, response));
});

Given('los remitos entregados', function () {
    // Write code here that turns the phrase above into concrete actions
    return true;
});

When('se solicita facturar con fecha {string}', function (fechaFactura) {
    // Write code here that turns the phrase above into concrete actions
    let res;
    try {
        res = request('POST', `${url}/facturas`, { json: fechaFactura });
        response = JSON.parse(res.getBody('utf8'));

        if (response.StatusCode == 200) {
            return true;
        } else {
            return assert.fail(response.StatusText);
        }

    } catch (error) {
        return assert.fail(error.message);
    }
});

Then('se obtiente el siguiente resultado de facturación', function (resultadoEsperado) {
    // Write code here that turns the phrase above into concrete actions
    const json = JSON.parse(resultadoEsperado);
    return assert.equal(undefined, jsondiff.diff(json, response));
});


Given('se generaron las siguientes facturas', function (facturasDT) {
    // Write code here that turns the phrase above into concrete actions

    let facturas = facturasDT.hashes();
    try {
        for (let i = 0; i < facturas.length; i++) {
            let res = request('GET', `${url}/facturas/${facturas[i].Factura}`);
            let body = JSON.parse(res.getBody('utf8'));

            if (facturas[i].Factura != body.data.id) {
                return assert.fail("La factura " + facturas[i].Factura + " no coincide con " + body.data.id + ".");
            }
            if (facturas[i].FechaEmision != body.data.fechaEmision) {
                return assert.fail("La fecha de la factura " + facturas[i].Factura + " no coincide con " + body.data.fechaEmision + ".");
            }
            if (facturas[i].Cliente != body.data.cliente.id) {
                return assert.fail("El cliente de la factura " + facturas[i].Factura + " no coincide con " + body.data.cliente.id + ".");
            }
            if (facturas[i].Total != body.total) {
                return assert.fail("El total de la factura " + facturas[i].Factura + " no coincide con " + body.total + ".");
            }
        }
        return true;
    } catch (error) {
        return assert.fail(error.message);
    }

});

let newFactura;
Given('la factura {int} que no ha sido abonada aún', function (int) {
    // Write code here that turns the phrase above into concrete actions
    try {

        let res = request('GET', `${url}/facturas/${int}`);
        let body = JSON.parse(res.getBody('utf8'));

        if (body.StatusCode = 200) {
            if (body.data.fechaPago == null) {
                newFactura = body.data;
                return true;
            } else {
                return assert.fail("La factura " + int + " ya ha sido abonada.");
            }

        } else {
            return assert.fail("La factura " + int + " no existe.");
        }

    } catch (error) {
        return assert.fail(error.message);
    }
});

When('la factura número {int} se marca como abonada en la fecha {string}', function (int, string) {
    // Write code here that turns the phrase above into concrete actions

    try {
        newFactura.fechaPago = string;
        const res = request('PUT', `${url}/facturas`, {
            json: newFactura
        });
        response = JSON.parse(res.getBody('utf8'));
        return true;

    } catch (error) {
        return assert.fail(error.message);
    }

});

Then('se obtiene el siguiente {string} de facturas abonadas', function (resultadoEsperado) {
    // Write code here that turns the phrase above into concrete actions
    let json = JSON.parse(resultadoEsperado);
    return assert.equal(undefined, jsondiff.diff(json, response));
});


Given('la factura {string} que no existe', function (id) {
    // Write code here that turns the phrase above into concrete actions
    let res;
    try {
        res = request('GET', `${url}/facturas/${id}`);
        response = JSON.parse(res.getBody('utf8'));

        if (response.StatusCode = 501) {
            return true;
        } else {
            return assert.fail(response.StatusText);
        }

    } catch (error) {
        return assert.fail(error.message);
    }

});

When('se intenta marcar como abonada en la fecha {string}', function (fecha) {
    // Write code here that turns the phrase above into concrete actions
    return true;
});

Then('se obtiene la siguiente respuesta', function (aRespuesta) {
    // Write code here that turns the phrase above into concrete actions
    const json = JSON.parse(aRespuesta);  
    return assert.equal(undefined, jsondiff.diff(json, response));
});

