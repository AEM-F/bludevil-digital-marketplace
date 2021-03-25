export interface PaginationResponse<T>{
  objectsList:T[];
  totalPages:number;
  pageNumber:number;
  pageSize:number;
}
