import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { UsersRoutingModule } from './users-routing-module';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    HttpClientModule,
    UsersRoutingModule
  ]
})
export class UsersModule { }
