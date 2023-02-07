import { Component } from "@angular/core";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";

  @Component({
    selector: "ngbd-modal-confirm",
    template: `
    <div class="modal-header">
      <h4 class="modal-title" id="modal-title">{{title}}</h4>
      <button type="button" class="close" aria-describedby="modal-title" (click)="modal.dismiss()">
          <span aria-hidden="true">&times;</span>
      </button>
    </div>
    <div class="modal-body">
      <p>
          <strong>{{message}}</strong>
      </p>
      <p *ngIf="description">
          {{description}}
      </p>
    </div>
    <div class="modal-footer">
      <button type="button" class="btn btn-outline-secondary" (click)="modal.dismiss()">
          Cancelar
      </button>
      <button type="button" class="btn btn-danger" (click)="modal.close()">
          Ok
      </button>
    </div>
    `,
  })
  export class NgbdModalConfirm {
    constructor(public modal: NgbActiveModal) {}
  }