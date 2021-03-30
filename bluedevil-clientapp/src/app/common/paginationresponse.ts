export interface PaginationResponse<T>{
  objectsList:T[];
  totalElements:number;
  pageNumber:number;
  pageSize:number;
}
