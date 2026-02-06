import { Component } from '@angular/core';

// @Component({
//   selector: 'app-posts',
//   imports: [],
//   templateUrl: '',
//   styleUrl: './posts.scss',
// })
export class Posts {}

/*
@Injectable({
  providedIn: 'root'
})
export class PostService {
  private apiUrl = 'https://api.example.com/posts';

  constructor(private http: HttpClient) {}

  // Fetch all posts
  getPosts(): Observable<Post[]> {
    return this.http.get<Post[]>(this.apiUrl);
  }

  // Fetch a single post by ID
  getPostById(id: string): Observable<Post> {
    return this.http.get<Post>(`${this.apiUrl}/${id}`);
  }

  // Create a new post
  createPost(postData: Post): Observable<Post> {
    return this.http.post<Post>(this.apiUrl, postData);
  }

  // Delete a post
  deletePost(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}

*/
