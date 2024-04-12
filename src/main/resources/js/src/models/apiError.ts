export default interface ApiError {
    status: 'NOT_FOUND'|'BAD_REQUEST'|string,
    timestamp: string,
    message: string,
    subErrors?: SubError[]
}
export interface SubError {
    object: string,
    field: string,
    rejectedValue: string,
    message: string
}